package com.github.Marko590;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public class Main {

        public static void main(String[] args) {


            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED);


            DiscordApi api = new DiscordApiBuilder()


                    .addServerBecomesAvailableListener(event -> {


                        System.out.println("Loaded " + event.getServer().getName());

                    })

                    .setToken(System.getenv("DiscordToken"))
                    .setWaitForServersOnStartup(false)
                    .login()
                    .join();

            api.addMessageCreateListener(event->{
                Message message=event.getMessage();


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

            });



            System.out.println(api.createBotInvite());
        }
    }

