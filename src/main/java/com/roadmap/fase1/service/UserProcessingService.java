package com.roadmap.fase1.service;

import com.roadmap.fase1.model.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class UserProcessingService {

    private final BlockingQueue<User> queue;

    @Autowired
    private EmailService emailService;

    public UserProcessingService(BlockingQueue<User> userQueue) {
        this.queue = userQueue;
    }

    public  void enqueueUser(User user) throws InterruptedException {
        queue.put(user);
    }

    @PostConstruct
    public void consumerQueue(){
        Thread thread = new Thread(()->{
           while(true){
               try {
                   User user = queue.take();
                   sendEmailUser(user);
               }
               catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
                   log.error(e.getMessage());
                   break;
               }
           }

        });
        thread.setDaemon(true);
        thread.start();

    }

    private void sendEmailUser(User user){
        String message = "Olá " + user.getName() + ", seu cadastro foi realizado com sucesso!";
        emailService.sendEmail(user.getEmail(),"Seja bem vindo!", message);
    }


}
