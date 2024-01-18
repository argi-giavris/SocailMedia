package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follower {

    Integer followerUserId;
    Integer userIdToFollow;

    public static Follower newFollowersRelationship(Integer followerUserId, Integer userIdToFollow) {
        return new Follower(followerUserId, userIdToFollow);
    }
}
