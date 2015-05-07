package com.test.testbuildtypedemo;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by rex.yau on 5/7/2015.
 */
public interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}

