package in.roadcast.ridersdk;

import io.realm.internal.Keep;

@Keep
public interface RoadcastDelegate
{
    void success();

    void failure(String error);
}
