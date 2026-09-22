package com.example.orderprocessing.service;

import org.springframework.stereotype.Component; import org.springframework.web.servlet.mvc.method.annotation.SseEmitter; import java.io.IOException; import java.util.concurrent.CopyOnWriteArrayList;
@Component public class EventHub {
  private final CopyOnWriteArrayList<SseEmitter> clients=new CopyOnWriteArrayList<>();
  public SseEmitter subscribe(){SseEmitter emitter=new SseEmitter(0L); clients.add(emitter); emitter.onCompletion(()->clients.remove(emitter)); emitter.onTimeout(()->clients.remove(emitter)); return emitter;}
  public void publish(Object event){for(SseEmitter client:clients){try{client.send(SseEmitter.event().name("order-update").data(event));}catch(IOException ex){clients.remove(client);}}}
}
