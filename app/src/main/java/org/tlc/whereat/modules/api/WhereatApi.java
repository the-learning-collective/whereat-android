/**
 *
 * Copyright (c) 2015-present, Total Location Test Paragraph.
 * All rights reserved.
 *
 * This file is part of Where@. Where@ is free software:
 * you can redistribute it and/or modify it under the terms of
 * the GNU General Public License (GPL), either version 3
 * of the License, or (at your option) any later version.
 *
 * Where@ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. For more details,
 * see the full license at <http://www.gnu.org/licenses/gpl-3.0.en.html>
 *
 */

package org.tlc.whereat.modules.api;

import org.tlc.whereat.model.ApiMessage;
import org.tlc.whereat.model.UserLocationTimestamped;
import org.tlc.whereat.model.UserLocation;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Author: @aguestuser
 * License: GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)
 */

public interface WhereatApi {

    @POST("/locations/update")
    Observable<List<UserLocation>> update(@Body UserLocationTimestamped ult);

    @POST("/locations/clear")
    Observable<ApiMessage> remove(@Body UserLocation ul);

}