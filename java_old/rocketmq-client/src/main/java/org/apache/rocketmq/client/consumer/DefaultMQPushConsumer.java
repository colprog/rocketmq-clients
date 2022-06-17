/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.client.consumer;

import javax.annotation.concurrent.ThreadSafe;
import org.apache.rocketmq.client.consumer.filter.ExpressionType;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.ClientException;
import org.apache.rocketmq.client.impl.consumer.OffsetStore;
import org.apache.rocketmq.client.impl.consumer.PushConsumerImpl;
import org.apache.rocketmq.client.remoting.CredentialsProvider;
import org.apache.rocketmq.client.trace.TracingMessageInterceptor;

/**
 * This class is the entry point for applications intending to consume messages using <strong>push</strong> mode.
 *
 * <p>It's fine to tune fields which exposes getter/setter methods, but keep in mind, all of them should work well
 * out of box for most scenarios.
 *
 * <p>This class allows user to custom the {@link MessageListener}, which would delivery message in time according to
 * defined consumption policy.
 */
@ThreadSafe
public class DefaultMQPushConsumer {

    /**
     * Wrapping internal implementations for virtually all methods presented in this class.
     */
    protected final PushConsumerImpl impl;

    /**
     * Constructor specifying group.
     *
     * @param group group name.
     * @throws ClientException if there is any client error.
     */
    public DefaultMQPushConsumer(final String group) throws ClientException {
        this.impl = new PushConsumerImpl(group);
    }

    /**
     * Set name of consumer group.
     *
     * @param group name of consumer group.
     * @throws ClientException if there is any client error.
     */
    public void setConsumerGroup(String group) throws ClientException {
        impl.setGroup(group);
    }

    /**
     * Get name of consumer group.
     *
     * @return name of consumer group.
     */
    public String getConsumerGroup() {
        return this.impl.getGroup();
    }

    /**
     * This method gets internal infrastructure readily to serve. Instances must call this method after configuration.
     */
    public void start() {
        this.impl.start();
    }

    /**
     * Shut down this client and releasing underlying resources.
     */
    public void shutdown() {
        this.impl.shutdown();
    }

    /**
     * Set address of name server.
     *
     * <p> 1. Example usage for ip list. 127.0.0.1:9876[;127.0.0.2:9875]
     * <p> 2. Example usage for domain name: http://MQ_INST_1973281269661160_BXmPlOA6.mq-aone.mq-internal.aliyuncs
     * .com:8081
     *
     * @param address address of name server.
     * @throws ClientException if there is any client error.
     */
    public void setNamesrvAddr(String address) throws ClientException {
        this.impl.setNamesrvAddr(address);
    }

    /**
     * Enable message tracing. If enabled, <a href="https://opentelemetry.io">OpenTelemetry</a>
     * would be enabled to record message tracing by span. See {@link TracingMessageInterceptor} for more details.
     *
     * @param enabled message tracing is enabled or not.
     */
    public void setMessageTracingEnabled(boolean enabled) {
        this.impl.setTracingEnabled(enabled);
    }


    public boolean getMessageTracingEnabled() {
        return this.impl.getTracingEnabled();
    }

    /**
     * Set message consumption threads amount.
     *
     * @param amount threads amount.
     */
    public void setConsumptionThreadsAmount(int amount) {
        this.impl.setConsumptionThreadsAmount(amount);
    }

    /**
     * Get message consumption threads amount.
     *
     * @return threads amount.
     */
    public int getConsumptionThreadsAmount() {
        return this.impl.getConsumptionThreadsAmount();
    }

    /**
     * Limit the consumption rate for each topic of consumer.
     *
     * @param topic            topic's name.
     * @param permitsPerSecond consumption rate for topic.
     * @since 5.0.0
     */
    public void rateLimit(String topic, double permitsPerSecond) {
        this.impl.rateLimit(topic, permitsPerSecond);
    }

    /**
     * Register message listener for concurrent consumption.
     *
     * @param listener concurrent message listener.
     */
    public void registerMessageListener(MessageListenerConcurrently listener) {
        this.impl.registerMessageListener(listener);
    }

    /**
     * Register message listener for order consumption.
     *
     * @param listener order message listener.
     */
    public void registerMessageListener(MessageListenerOrderly listener) {
        this.impl.registerMessageListener(listener);
    }

    /**
     * Subscribe topic to consume message, and provide expression of {@link ExpressionType#TAG} to filter message.
     *
     * @param topic      subscribed topic.
     * @param expression expression to filter message.
     */
    public void subscribe(String topic, String expression) {
        this.impl.subscribe(topic, expression, ExpressionType.TAG);
    }

    /**
     * Subscribe topic to consume message, and provide expression to filter message.
     *
     * @param topic          subscribed topic.
     * @param expression     expression to filter message.
     * @param expressionType expression type.
     */
    public void subscribe(String topic, String expression, ExpressionType expressionType) {
        this.impl.subscribe(topic, expression, expressionType);
    }

    /**
     * Unsubscribe topic to consume message, once topic was unsubscribed, it would stop to deliver message of this
     * topic.
     *
     * @param topic name of topic.
     */
    public void unsubscribe(String topic) {
        this.impl.unsubscribe(topic);
    }

    /**
     * Set abstract resource name of consumer.
     *
     * @param namespace abstract resource name.
     */
    public void setNamespace(String namespace) {
        this.impl.setNamespace(namespace);
    }

    /**
     * Get abstract resource namespace of consumer.
     *
     * @return abstract resource namespace.
     */
    public String getNamespace() {
        return this.impl.getNamespace();
    }

    /**
     * Set credentials provider for consumer.
     *
     * @param provider credentials provider.
     */
    public void setCredentialsProvider(CredentialsProvider provider) {
        this.impl.setCredentialsProvider(provider);
    }

    /**
     * Set message consumption max batch size.
     *
     * @param size max batch size.
     */
    public void setConsumeMessageBatchMaxSize(int size) {
        this.impl.setConsumeMessageBatchMaxSize(size);
    }

    /**
     * Get message consumption max batch size.
     *
     * @return max batch size.
     */
    public int getConsumeMessageBatchMaxSize() {
        return this.impl.getConsumeMessageBatchMaxSize();
    }

    /**
     * Set max await time for each queue.
     *
     * @param timeMillis await time.
     */
    public void setMaxAwaitTimeMillisPerQueue(long timeMillis) {
        this.impl.setMaxAwaitTimeMillisPerQueue(timeMillis);
    }

    /**
     * Set max await batch size for each queue.
     *
     * @param size max batch size.
     */
    public void setMaxAwaitBatchSizePerQueue(int size) {
        this.impl.setMaxAwaitBatchSizePerQueue(size);
    }

    public int getMaxAwaitBatchSizePerQueue() {
        return this.impl.getMaxAwaitBatchSizePerQueue();
    }

    /**
     * Set message consumption model for consumer.
     *
     * @param messageModel mode of message consumption model.
     */
    public void setMessageModel(MessageModel messageModel) {
        this.impl.setMessageModel(messageModel);
    }

    public MessageModel getMessageModel() {
        return this.impl.getMessageModel();
    }

    /**
     * Set the policy to consume from.
     *
     * @param consumeFromWhere policy to consume from
     */
    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.impl.setConsumeFromWhere(consumeFromWhere);
    }

    /**
     * Get the policy to consume from
     *
     * @return consumeFromWhere policy to consume from
     */
    public ConsumeFromWhere getConsumeFromWhere() {
        return this.impl.getConsumeFromWhere();
    }

    /**
     * Set message max delivery attempt times.
     *
     * @param maxAttempts max attempts.
     */
    public void setMaxDeliveryAttempts(int maxAttempts) {
        this.impl.setMaxDeliveryAttempts(maxAttempts);
    }

    /**
     * Get message max delivery attempt times.
     *
     * @return max attempts.
     */
    public int getMaxDeliveryAttempts() {
        return this.impl.getMaxDeliveryAttempts();
    }

    /**
     * Set fifo suspend time before the next delivery while encounter failure of consumption.
     *
     * @param time suspend time.
     */
    public void setFifoConsumptionSuspendTimeMillis(long time) {
        this.impl.setFifoConsumptionSuspendTimeMillis(time);
    }

    public long getFifoConsumptionSuspendTimeMillis() {
        return this.impl.getFifoConsumptionSuspendTimeMillis();
    }

    /**
     * Set max quantity threshold of total cached messages.
     *
     * @param quantity message quantity threshold.
     */
    public void setMaxTotalCachedMessagesQuantityThreshold(int quantity) {
        this.impl.setMaxTotalCachedMessagesQuantityThreshold(quantity);
    }

    /**
     * Get max quantity threshold of total cached messages.
     *
     * @return max quantity threshold.
     */
    public int getMaxTotalCachedMessagesQuantityThreshold() {
        return this.impl.getMaxTotalCachedMessagesQuantityThreshold();
    }

    /**
     * Set max bytes threshold of bodies of total cached messages.
     *
     * @param bytes bytes of bodies of messages.
     */
    public void setMaxTotalCachedMessagesBytesThreshold(int bytes) {
        this.impl.setMaxTotalCachedMessagesBytesThreshold(bytes);
    }

    /**
     * Get max bytes threshold of bodies of total cached messages.
     *
     * @return bytes of bodies of messages.
     */
    public int getMaxTotalCachedMessagesBytesThreshold() {
        return this.impl.getMaxTotalCachedMessagesBytesThreshold();
    }

    /**
     * Set timeout of message consumption.
     *
     * @param timeout consumption timeout.
     */
    public void setConsumptionTimeoutMillis(long timeout) {
        this.impl.setConsumptionTimeoutMillis(timeout);
    }

    /**
     * Get timeout of message consumption.
     *
     * @return consumption timeout.
     */
    public long getConsumptionTimeoutMillis() {
        return this.impl.getConsumptionTimeoutMillis();
    }

    public void setOffsetStore(OffsetStore offsetStore) {
        this.impl.setOffsetStore(offsetStore);
    }
}