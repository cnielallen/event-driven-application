package com.cnielallen.eventdriven.config;

import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.String.join;

@Slf4j
public class SolaceContainer extends GenericContainer<SolaceContainer> {
    private static final int DEFAULT_SOLACE_MESSAGE_FORMAT_PORT = 55555;
    private static final int DEFAULT_UI_PORT = 8080;
    private static final int DEFAULT_MQTT_PORT = 8000;

    private final List<List<String>> commands = new ArrayList<>();


    public static String JOURNAL_SERVICE_SUBSCRIBE_QUEUE = "journal_queue";
    public static String JOURNAL_SERVICE_TOPIC = "journal_topic";
    public static String JOURNAL_GROUP = "journal_group";

    public static String PUBLISH_JOURNAL_TOPIC = "journal_publish_topic";

    private static final Map<String,String> TOPICS_BY_QUEUS = Map.ofEntries(
      Map.entry(join(".", JOURNAL_SERVICE_SUBSCRIBE_QUEUE, JOURNAL_GROUP), JOURNAL_SERVICE_TOPIC)
    );

    public static String JOURNAL_SERVICE_SUBSCRIBE_QUEUE_DMQ = "journal_queue.journal_group.dmq";

    private static final List<String> DMQ_QUEUES = List.of(
      JOURNAL_SERVICE_SUBSCRIBE_QUEUE_DMQ
    );

    public SolaceContainer(){
        this("/solace/solace-pubsub-standard:latest");
    }

    public SolaceContainer(String image) {
        super(image);

        withSharedMemorySize(2024 * FileUtils.ONE_MB);
        addExposedPorts(DEFAULT_SOLACE_MESSAGE_FORMAT_PORT, DEFAULT_UI_PORT, DEFAULT_MQTT_PORT);
        this.waitStrategy = new HostPortWaitStrategy()
                .withStartupTimeout(Duration.ofSeconds(90));
    }

    @Override
    protected void configure() {
        addEnv("username_admin_globalaccesslevel", "admin");
        addEnv("username_admin_password", "admin");
    }

    @SneakyThrows
    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        commands.forEach(command->{
            try{
                ExecResult execResult = execInContainer(command.toArray(new String[0]));
                if(execResult.getExitCode()!=0){
                    logger().error("Could not execute command {}: {}", command, execResult.getStderr());
                }
            } catch(InterruptedException | IOException e) {
                logger().error("Could not execute command {}: {}", command, e.getMessage());
            }
        });

        Flux.concat(createTopicRequest(), createQueueRequest(), createSubscriptionRequest())
                .doOnNext(request -> sendRequestToSolace(request.url, request.getJson())).blockLast();
    }

    private Flux<SolaceConfigRequest> createTopicRequest(){
        var  topics = new ArrayList<>(TOPICS_BY_QUEUS.values());
        return Flux.fromIterable(topics.stream().map(
          topicName -> {
              var url = "http://localhost:" + getUIPort() + "/SEMP/v2/config/msgVpns/default" +
                      "/topicEndpoints";
              var json = "{\"topicEndpointName\":\"" + topicName + "\",\"accessTpe\":\"non" +
                      "-exclusive\",\"egressEnabled\":true,\"ingressEnabled\":true," +
                      "\"permission\":\"delete\"}";
              return new SolaceConfigRequest(url, json);
          }).collect(Collectors.toList()));
    }

    private Flux<SolaceConfigRequest> createQueueRequest(){
        return Flux.fromIterable(
                TOPICS_BY_QUEUS.keySet()).concatWith(Flux.fromIterable(DMQ_QUEUES))
                .map(queueName -> {
                    var url = "http://localhost:" + getUIPort() + "/SEMP/v2/config/msgVpns/default/queues";
                    var json = "{\"queueName\":\"" + queueName + "\",\"accessTpe\":\"non" +
                            "-exclusive\",\"egressEnabled\":true,\"ingressEnabled\":true," +
                            "\"permission\":\"delete\"}";
                    return new SolaceConfigRequest(url, json);
                });
    }

    private Flux<SolaceConfigRequest> createSubscriptionRequest(){
        return Flux.fromIterable(
               TOPICS_BY_QUEUS.entrySet().stream().map(entry -> {
                    var queueName = entry.getKey();
                    var topicName = entry.getValue();
                    var url = "http://localhost:" + getUIPort() + "/SEMP/v2/config/msgVpns/default/queues/" + queueName + "/subscriptons";
                    var json = "{\"queueName\":\"" + queueName + "\",\"subscriptionTopic\":\"" + topicName + "\"}";
                    return new SolaceConfigRequest(url, json);
               }).collect(Collectors.toList()));
    }

    private void sendRequestToSolace(String url, String json){
        log.info("Sending request to solace, url: {}, json {}", url, json);
        WebClient.create().post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .header(HttpHeaders.AUTHORIZATION,
                    "basic" + Base64Utils.encodeToString(("admin:admin").getBytes()))
        .exchangeToMono( response-> {
           if( response.rawStatusCode() == 200) {
               log.info("Request is completed successfully. url {}, json: {}", url, json);
           } else {
               log.info("Retrying request url: {}, json: {}", url,json);
               try {
                   TimeUnit.SECONDS.sleep(1);
               } catch(InterruptedException e) {
                   log.warn("Could not wait for one second before retrying to send request. url: {}, json: {}", url, json);
               }
               sendRequestToSolace(url,json);
           }
           return response.bodyToMono(Object.class);
        });
    }



    public Integer getSMFPort() { return getMappedPort( DEFAULT_SOLACE_MESSAGE_FORMAT_PORT);}
    public Integer getUIPort() { return  getMappedPort(DEFAULT_UI_PORT);}
    public Integer getMqttPort() { return  getMappedPort(DEFAULT_MQTT_PORT);}
    public String getSMFUrl() { return "tcp://" + getContainerIpAddress() + ":" + getSMFPort();}

    @RequiredArgsConstructor
    @Getter
    private class SolaceConfigRequest {
        private final String url;
        private final String json;
    }
}
