package com.github.Marko590;

import javafx.util.Pair;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;


import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class QueueHandler {

    private LinkedList<User> mainQueue;
    private TextChannel targetTextChannel;
    private Random random =new Random();


    private EmbedBuilder jokeEmbed = new EmbedBuilder()
            .setImage("https://pbs.twimg.com/profile_images/1339642265090875392/zdjZK5e0_400x400.jpg");
    private EmbedBuilder helpEmbed = new EmbedBuilder()
            .setColor(Color.RED)
            .setTitle("Queue-Bot options")
            .addField("-queue", "Adds you to the current queue")
            .addField("-fivestack" , "Removes the first 5 users from the queue")
            .addField("-dequeue","Removes you from the queue")
            .addField("-list","Lists the users in the queue without mentioning them")
            .addField("-ping","Lists the users in the queue and mentions them")
            .addField("-clear","Forcefully clears the list of the players in queue")
            .addField("-getmonke","Removes 5 random monkes from the queue");


     public QueueHandler() {
         this.mainQueue=new LinkedList<>();
     }
     public void initialiseTextChannel(TextChannel targetTextChannel){
         this.targetTextChannel=targetTextChannel;
         sendMessage("Text channel saved");
     }
     public void sendMessage(String message){
         new MessageBuilder()
                 .append(message)
                 .send(targetTextChannel);
     }

     public void accessibilityMetod(){
         new MessageBuilder()
                 .setEmbed(jokeEmbed)
                 .send(targetTextChannel);
     }
    public void sendMentionsToUsers(String message){
        sendMessage(message+'\n'+getMentionableTags());
    }

     public void addUserToQueue(User user){

         for(User u: mainQueue){
             if(u.getIdAsString().equalsIgnoreCase(user.getIdAsString())){
                 sendMessage("You are already queued for a match.");
                 return;
             }

         }
         mainQueue.add(user);
         sendMessage("You have sucecessfuly been added to the queue.");
         if(mainQueue.size()==5){
             sendMentionsToUsers("You have enough players to start a match! Type '-pop' to free up the queue.");
         }

     }
    public void listUsers(){
         StringBuilder sb=new StringBuilder();
         if(mainQueue.size()!=0) {
             for (User u : mainQueue) {
                 sb.append(u.getDiscriminatedName());
                 sb.append('\n');
             }
             sendMessage(sb.toString());
             return;
         }
         else{
             sendMessage("The queue is currently empty.");
         }
    }
    public void dequeueUser(User user,Boolean sendMessageCheck) {
         Boolean check=false;
         for (User u : mainQueue) {
             if (u.getIdAsString().equalsIgnoreCase(user.getIdAsString())) {
                 mainQueue.remove(u);
                 check=true;
                 break;
             }
         }
         if(check){
             if(sendMessageCheck)
             sendMessage("You have successfuly been dequeued.");
         }
         else{
             if(sendMessageCheck)
             sendMessage("You aren't in the queue.");
         }
     }

    private String getMentionableTags(){
        StringBuilder sb=new StringBuilder();
        for(User u:mainQueue){
            sb.append(u.getMentionTag());
            sb.append('\n');
        }
        return sb.toString();
    }

     public void popTeam(){
        User first;
        StringBuilder sb=new StringBuilder();
        sb.append("The following players have started a queue for a match:"+'\n');

         if(mainQueue.size()>5) {
             for (int i = 0; i < 5; i++) {
                 first=mainQueue.getFirst();
                 sb.append(first.getMentionTag()+'\n');
                 mainQueue.removeFirst();
             }
             sendMessage(sb.toString());
         }
         else if(mainQueue.size()!=0){
             for(User u:mainQueue){
                 sb.append(u.getMentionTag()+'\n');
             }
             mainQueue.clear();
             sendMessage(sb.toString());
         }
         else{
             sendMessage("The queue is currently empty.");
         }

     }

     public void forceQueueClear(){
         mainQueue.clear();
         sendMessage("The queue was successfuly cleared.");
     }

     public void listOptions(){
         new MessageBuilder()
                 .setEmbed(helpEmbed)
                 .send(targetTextChannel.asTextChannel().get());

     }

     public void pingUsers(){
         if(mainQueue.size()==0){
             sendMessage("The queue is currently empty.");
         }
         else{
             sendMentionsToUsers("");
         }
    }

     public void getRandomFiveStack(){

        if(mainQueue.size()<5){
            sendMessage("Not enough people for a random fivestack.");
        }
        else{
            StringBuilder sb=new StringBuilder();

            LinkedList<User> lucky=new LinkedList<>();
            for(int i=0;i<5;i++){
                lucky.add(mainQueue.get(random.nextInt(mainQueue.size())));
            }

            for(User u:lucky){
                sb.append(u.getMentionTag());
                sb.append("\n");
                dequeueUser(u,false);
            }
            sendMessage(sb.toString());

        }

     }
}
