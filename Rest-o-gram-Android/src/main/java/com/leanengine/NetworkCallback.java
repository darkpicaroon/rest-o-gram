/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

/**
 * Callback interface used when invoking REST service in the background thread.
 * @param <E>
 */
public interface NetworkCallback<E> {

    /**
     * REST service was successfully invoked and result is available.
     * @param result Result as returned by REST service.
     */
    public abstract void onResult(E... result);

    /**
     * There was an error invoking the REST service.
     * @param error A {@link LeanError} containing detailed error code and description of error.
     */
    public abstract void onFailure(LeanError error);

}
