package com.github.Marko590;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {




        DiscordApi api = new DiscordApiBuilder()


                .addServerBecomesAvailableListener(event -> {


                    System.out.println("Loaded " + event.getServer().getName());

                })

                .setToken(System.getenv("DiscordToken"))
                .setWaitForServersOnStartup(false)
                .login()
                .join();

        Optional<Server> optionalServer = api.getServerById("703957572159406090");

        //Checking if the bot is connected to the server
        if (!optionalServer.isPresent()) {
            System.err.println("Couldn't find the requested server");
            return;
        }
        Server server = optionalServer.get();

        QueueHandler queueHandler = new QueueHandler();
        api.addMessageCreateListener(event -> {
            Message message = event.getMessage();
            User messageAuthor = message.getUserAuthor().get();


            if (message.getContent().equalsIgnoreCase("-initialise")) {
                queueHandler.initialiseTextChannel(message.getChannel().asTextChannel().get());
            } else if (message.getContent().equalsIgnoreCase("-queue")) {
                queueHandler.addUserToQueue(messageAuthor);
            } else if (message.getContent().equalsIgnoreCase("-fivestack")) {
                queueHandler.popTeam();
            } else if (message.getContent().equalsIgnoreCase("-unqueue")) {
                queueHandler.dequeueUser(messageAuthor, true);
            } else if (message.getContent().equalsIgnoreCase("-list")) {
                queueHandler.listUsers();
            } else if (message.getContent().equalsIgnoreCase("-ping")) {
                queueHandler.pingUsers();
            } else if (message.getContent().equalsIgnoreCase("-help")) {
                queueHandler.listOptions();
            } else if (message.getContent().equalsIgnoreCase("-clear")) {
                queueHandler.forceQueueClear();
            } else if (message.getContent().equalsIgnoreCase("-getmonke")) {
                queueHandler.getRandomFiveStack();
            } else if (message.getContent().equalsIgnoreCase("-golira")) {
                queueHandler.accessibilityMetod();
            } else if (message.getContent().equalsIgnoreCase("-counter") || message.getContent().equalsIgnoreCase("-count")) {
                queueHandler.goliraCounter();
            }



                 /*
                ChannelHandler master=new ChannelHandler(api);

                if(message.getContent().equalsIgnoreCase("!create")) {
                    master.createFacultyChannels("matf", "Matematicki Fakultet");
                    master.createFacultyChannels("etf", "Elektrotehnicki fakultet");
                }

                else if(message.getContent().equalsIgnoreCase("!assign")) {
                    master.AddUserToFaculty("372728675407495178", "matf");
                    master.AddUserToFaculty("778381277761503263", "etf");
                }
                else if(message.getContent().equalsIgnoreCase("!teams")) {
                    master.addTeamtoFaculty("ayy lmao", "https://www.youtube.com/watch?v=O4irXQhgMqg&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMgItFOW551qc&index=17&ab_channel=ABKCOVEVO", "matf");
                }
                else if(message.getContent().equalsIgnoreCase("!announce")){
                    master.announceTournament("EHS","https://www.youtube.com/watch?v=4Qpg6iVqNWs&list=RDGMEMJQXQAmqrnmK1SEjY_rKBGAVMgItFOW551qc&index=19&ab_channel=MarkieMusic");
                }
                */
        });


        System.out.println(api.createBotInvite());
    }
}

