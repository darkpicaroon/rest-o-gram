package com.tau.tasks;

import com.tau.RestogramPhoto;
import com.tau.RestogramVenue;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 05/04/13
 */
public interface ITaskObserver {

    void onFinished(RestogramVenue[] venues);

    void onFinished(RestogramVenue venue);

    void onFinished(RestogramPhoto[] photos);
}