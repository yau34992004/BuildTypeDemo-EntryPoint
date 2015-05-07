package com.test.testbuildtypedemo;

/**
 * Created by rex.yau on 5/7/2015.
 */

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit.MockRestAdapter;
import retrofit.RestAdapter;

/**
 * // Copyright 2013 Square, Inc.
 */
public class GithubClient {


    /**
     * A mock implementation of the {@link GitHub} API interface.
     */
    static class MockGitHub implements GitHub {
        private final Map<String, Map<String, List<Contributor>>> ownerRepoContributors;

        public MockGitHub() {
            ownerRepoContributors = new LinkedHashMap<String, Map<String, List<Contributor>>>();

            // Seed some mock data.
            addContributor("square", "retrofit", "John Doe", 12);
            addContributor("square", "retrofit", "Bob Smith", 2);
            addContributor("square", "retrofit", "Big Bird", 40);
            addContributor("square", "picasso", "Proposition Joe", 39);
            addContributor("square", "picasso", "Keiser Soze", 152);
        }

        @Override
        public List<Contributor> contributors(String owner, String repo) {
            Map<String, List<Contributor>> repoContributors = ownerRepoContributors.get(owner);
            if (repoContributors == null) {
                return Collections.emptyList();
            }
            List<Contributor> contributors = repoContributors.get(repo);
            if (contributors == null) {
                return Collections.emptyList();
            }
            return contributors;
        }

        public void addContributor(String owner, String repo, String name, int contributions) {
            Map<String, List<Contributor>> repoContributors = ownerRepoContributors.get(owner);
            if (repoContributors == null) {
                repoContributors = new LinkedHashMap<String, List<Contributor>>();
                ownerRepoContributors.put(owner, repoContributors);
            }
            List<Contributor> contributors = repoContributors.get(repo);
            if (contributors == null) {
                contributors = new ArrayList<Contributor>();
                repoContributors.put(repo, contributors);
            }
            contributors.add(new Contributor(name, contributions));
        }
    }

    public GithubClient(Context context) {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.end_point))
                .build();

        // Wrap our REST adapter to allow mock implementations and fake network delay.
        MockRestAdapter mockRestAdapter =
                MockRestAdapter.from(restAdapter);

        // Instantiate a mock object so we can interact with it later.
        MockGitHub mockGitHub = new MockGitHub();
        // Use the mock REST adapter and our mock object to create the API interface.
        GitHub gitHub = mockRestAdapter.create(GitHub.class, mockGitHub);

        // Query for some contributors for a few repositories.
        printContributors(gitHub, "square", "retrofit");
        printContributors(gitHub, "square", "picasso");

        // Using the mock object, add some additional mock data.
        System.out.println("Adding more mock data...\n");
        mockGitHub.addContributor("square", "retrofit", "Foo Bar", 61);
        mockGitHub.addContributor("square", "picasso", "Kit Kat", 53);

        // Query for the contributors again so we can see the mock data that was added.
        printContributors(gitHub, "square", "retrofit");
        printContributors(gitHub, "square", "picasso");
    }

    private static void printContributors(GitHub gitHub, String owner, String repo) {
        System.out.println(String.format("== Contributors for %s/%s ==", owner, repo));
        List<Contributor> contributors = gitHub.contributors(owner, repo);
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
        System.out.println();
    }
}