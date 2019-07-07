package in.roadcast.ridersdk;

import io.realm.annotations.RealmModule;
import io.realm.internal.Keep;

//classes = {Cat.class, Dog.class}
@Keep
@RealmModule(library = true, allClasses = true)
public class RealmModules {
}


