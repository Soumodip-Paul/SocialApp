package com.sp.SPBrowser.textHandler.model;

import androidx.annotation.NonNull;

public class User {
    public String UserName,uId,imageUri,email,phoneNumber;
    public User(String uId,String Username,String imageUri,String email,@NonNull String phoneNumber){
       this.UserName = Username;
       this.uId = uId;
       this.imageUri = imageUri;
       this.email = email;
       this.phoneNumber = phoneNumber;

   }
   public  User(){}
}
