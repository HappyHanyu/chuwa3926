Q1.
For a payment system, the goal should be exactly-once effect at the business level. In practice, this is usually implemented as at-least-once delivery + idempotent processing + transactional state update.
If you use at-most-once, the message may be lost. For example, Kafka may mark the message as consumed, but the payment service crashes before deducting money. Then the user paid, but the system never finishes the deduction or order update.
If you use at-least-once without idempotency, duplicate processing can happen. For example, the same $100 payment message may be consumed twice, so the user could be charged twice.

A practical exactly-once design is:
Put a unique paymentId or orderId in the message.
In the payment database, keep a processed-message table or idempotency table.
Consume the message in a transaction:
check whether paymentId has already been processed
if not, deduct money and mark the message as processed
commit the DB transaction
Commit the Kafka offset only after the transaction succeeds.

That gives you exactly-once business effect, even if the message is delivered more than once.

Q2.
When getConnection() is called and the pool is empty while usedConnections.size() == maxPoolSize, this code will wait for a connection to be returned.

Flow:
availableConnections.poll() returns null.
Because usedConnections.size() is already at maxPoolSize, it cannot create a new connection.
It calls availableConnections.poll(maxWaitMs, TimeUnit.MILLISECONDS).
If another thread returns a connection before the timeout, this thread gets it.
If no connection is returned in time, it throws:
SQLException("Timeout waiting for connection")

So the behavior is: block and wait up to maxWaitMs, then fail with timeout if still no connection is available.

Q3.
The problem is that the unlock logic is not atomic.
Current logic: GET resource, compare value with lockId, if equal, DEL resource.

The race condition is:
client A reads the value and sees its own lockId
before A deletes it, the lock expires
client B acquires the same lock with a new value
client A then runs DEL and deletes B’s lock by mistake

The fix is to use an atomic compare-and-delete operation, usually with a Lua script in Redis:
public void unlock(String resource, String lockId) {
    String script =
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "   return redis.call('del', KEYS[1]) " +
        "else " +
        "   return 0 " +
        "end";

    try (Jedis jedis = pool.getResource()) {
        jedis.eval(script,
                Collections.singletonList(resource),
                Collections.singletonList(lockId));
    }
}
This ensures only the owner of the lock can delete it, and the comparison + delete happen atomically.

Q4.
The correct match is:
CLOSED → C
Normal operation, requests are allowed through, failures are being counted.
OPEN → B
All requests are blocked immediately without calling the downstream service.
HALF_OPEN → A
Allows a limited number of test requests to check if the service has recovered.

Q5.
The purpose of the quorum in Redlock is to make sure the lock is acquired on a majority of Redis nodes, not just one node. This reduces the chance of incorrect locking caused by node failure, split brain, or delayed replication.
If there are 5 Redis nodes, the minimum quorum should be: 3 Because quorum must be more than half.

Q6.
This implementation gives at-least-once semantics because the offset is committed after processing returns.

Why it is not exactly-once:
if processOrder(order) throws an exception, the offset is not committed, so Kafka can redeliver the message
if processing succeeds but the consumer crashes before offset commit, Kafka may also redeliver the message later

That means the message may be processed more than once, which is exactly at-least-once.

Q7.
Test-on-borrow means the pool validates the connection every time a client asks for one.
Background validation means the pool validates connections periodically in the background, even before clients request them.

Difference:
test-on-borrow is safer at checkout time because it checks the connection immediately before use
background validation reduces request-time overhead because validation happens asynchronously

Which has better performance?
Usually background validation has better performance, because it avoids adding validation latency to every getConnection() call.

Trade-off:
test-on-borrow = safer, but slower per request
background validation = faster, but there is a small chance a bad connection slips through between validation runs

Q8.
ZooKeeper distributed lock watches the node immediately before itself to avoid the herd effect.
If every waiting client watched all nodes or watched the head node, then when one lock is released, many clients would wake up at the same time and all try to acquire the lock. That creates unnecessary load.

By only watching the predecessor:
only the next eligible client wakes up
the wake-up chain is orderly
ZooKeeper gets much less notification traffic

So watching the node before itself improves both fairness and efficiency.

Q11.
To implement at-least-once with manual offset commit, disable auto-commit and acknowledge only after successful processing.
@KafkaListener(topics = "events", containerFactory = "kafkaListenerContainerFactory")
public void consume(Event event, Acknowledgment ack) {
    eventProcessor.process(event);  // if this throws, no ack
    ack.acknowledge();              // commit only after success
}
Configuration example:
@Bean
public ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerContainerFactory(
        ConsumerFactory<String, Event> consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<String, Event> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(consumerFactory);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
}
And in config:
spring.kafka.consumer.enable-auto-commit=false

Why this is at-least-once:
if processing fails, offset is not committed, so Kafka will retry
if processing succeeds, then we commit
duplicates are still possible, so it is not exactly-once