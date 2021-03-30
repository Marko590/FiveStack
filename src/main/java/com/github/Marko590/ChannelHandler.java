package com.github.Marko590;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.permission.*;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChannelHandler {
    private DiscordApi api = null;



    public ChannelHandler(DiscordApi api) {
        this.api = api;
    }

    public void createFacultyChannels(String FacultyId, String FacultyName) {
        Optional<Server> optional_server = api.getServerById("778381434590855209");
        boolean roleExists=false;


        if (optional_server.isPresent()) {

            Permissions facultyPermissions = new PermissionsBuilder()
                    .setAllDenied()
                    .setState(PermissionType.ADD_REACTIONS, PermissionState.ALLOWED)
                    .setState(PermissionType.ATTACH_FILE, PermissionState.ALLOWED)
                    .setState(PermissionType.CONNECT, PermissionState.ALLOWED)
                    .setState(PermissionType.EMBED_LINKS, PermissionState.ALLOWED)
                    .setState(PermissionType.READ_MESSAGES, PermissionState.ALLOWED)
                    .setState(PermissionType.READ_MESSAGE_HISTORY, PermissionState.ALLOWED)
                    .setState(PermissionType.SEND_MESSAGES, PermissionState.ALLOWED)
                    .setState(PermissionType.SEND_TTS_MESSAGES, PermissionState.ALLOWED)
                    .setState(PermissionType.SEND_TTS_MESSAGES, PermissionState.ALLOWED)
                    .setState(PermissionType.SPEAK, PermissionState.ALLOWED)
                    .setState(PermissionType.USE_VOICE_ACTIVITY, PermissionState.ALLOWED)
                    .build();
            Role  role=null;
            for(Role i:api.getRoles()) {
                if (i.getName().compareTo(FacultyId)==0){
                    return ;
                }
            }

            role = new RoleBuilder(optional_server.get())
                    .setMentionable(true)
                    .setColor(Color.RED)
                    .setName(FacultyId)
                    .setPermissions(facultyPermissions)
                    .create()
                    .join();


            RoleUpdater roleUpdater = new RoleUpdater(role);
            roleUpdater.update();

            ChannelCategory category = new ChannelCategoryBuilder(optional_server.get())
                    .setName(FacultyName+" "+FacultyId)
                    .create()
                    .join();

            ServerVoiceChannel facultyVoiceChannel = new ServerVoiceChannelBuilder(optional_server.get())
                    .setName(FacultyId)
                    .setUserlimit(10)
                    .setCategory(category)
                    .create()
                    .join();


            ServerVoiceChannel guestVoiceChannel = new ServerVoiceChannelBuilder(optional_server.get())
                    .setName(FacultyId + "'s lobby")
                    .setUserlimit(10)
                    .setCategory(category)
                    .create()
                    .join();

            ServerTextChannel textChannel = new ServerTextChannelBuilder(optional_server.get())
                    .setName(FacultyId + " text channel")
                    .setCategory(category)
                    .create()
                    .join();


        }

    }


public void AddUserToFaculty(String UserId,String FacultyId){

        Optional<Server> optionalServer=api.getServerById("778381434590855209");


        //Checking the connection to the server
        if(!optionalServer.isPresent()){
            System.err.println("Couldn't find the requested server");
            return;
        }

        //Checking to see if the role exists
        Role role=null;
        for(Role i:api.getRoles()){
            if(i.getName().compareTo(FacultyId)==0){
                role=i;
            }
        }

        if(role==null){
            System.err.println("Role does not exist yet");
            return;
        }


        CompletableFuture<User> newUser=api.getUserById(UserId);

        //Checking if the user already has the role



        for(Role i:newUser.join().getRoles(optionalServer.get())){
            System.out.println(i.getName());
            if(i.getName().compareToIgnoreCase(FacultyId)==0){

                System.err.println("User is already assigned to the requested role.");
                return;
            }
        }


        role.addUser(newUser.join());

        new MessageBuilder()
                .append("Welcome user ")
                .append(newUser.join().getMentionTag())
                .append(" to the "+FacultyId.toUpperCase()+" faculty discord!")
                .send(getFacultyTextChannel(FacultyId).asTextChannel().get());





}

private TextChannel getFacultyTextChannel(String FacultyId){

    TextChannel returnValue=null;
    //Locating the faculty category that recieves the welcome message for the new user
    Collection<ChannelCategory> categoryList=api.getChannelCategories();
    ChannelCategory welcomeTarget=null;
    for(ChannelCategory category: categoryList){
        if(category.getName().contains(FacultyId)){
            welcomeTarget=category;
        }
    }

    //Finding the textchannel within the category and sending the messages
    for(Channel channel: welcomeTarget.getChannels()){

        if(channel.getType().isTextChannelType()){

            if(channel.asTextChannel().isPresent()){

                returnValue=channel.asTextChannel().get();

            }

        }
    }
    return returnValue;
}


public void addTeamtoFaculty(String TeamName,String TeamPageLink,String FacultyId){
        new MessageBuilder()
                .append("Team " + TeamName + " has been created, visit their page at "+ TeamPageLink+" for more info.")
                .send(getFacultyTextChannel(FacultyId));

}

public void announceTournament(String TournamentName,String TournamentPageLink){
        new MessageBuilder()
                .append("@everyone The tournament "+ TournamentName+" has been announced, visit " + TournamentPageLink+ " for more info.")
                .send(api.getChannelById("778381434590855212").get().asTextChannel().get());

}

}
