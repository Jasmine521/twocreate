package com.example.twocreate.bean;

public record AuthToken(Long userId,long expiresAt) {
    public boolean isExpired(){
        return System.currentTimeMillis() > expiresAt();
    }
    public boolean isAboutToExpire(){
        return expiresAt() - System.currentTimeMillis() < 1800_000;
    }
    public AuthToken refresh(){
        return new AuthToken(this.userId(),System.currentTimeMillis()+3600_000);
    }
    /**
     * hash = hmacSha256(userId : expiresAt, hmacKey)
     *
     * secureString = userId : expiresAt : hash
     */
//    public String toSecureString(String hmacKey){
//        String payload = userId() + ":" + expiresAt();
//    }
}
