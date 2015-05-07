package com.test.testbuildtypedemo;

/**
 * Created by rex.yau on 5/7/2015.
 */

import android.content.Context;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * // Copyright 2013 Square, Inc.
 */
public class GithubClient {


    static class Contributor {
        String login;
        int contributions;
    }


    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }


    public GithubClient(Context context) {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.end_point))
                .build();


        // Create an instance of our GitHub API interface.
        GitHub github = restAdapter.create(GitHub.class);


        // Fetch and print a list of the contributors to this library.
        List<Contributor> contributors = github.contributors("square", "retrofit");
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
    }
}