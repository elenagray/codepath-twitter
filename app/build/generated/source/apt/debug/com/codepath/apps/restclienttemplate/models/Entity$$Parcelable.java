
package com.codepath.apps.restclienttemplate.models;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.IdentityCollection;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated("org.parceler.ParcelAnnotationProcessor")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class Entity$$Parcelable
    implements Parcelable, ParcelWrapper<com.codepath.apps.restclienttemplate.models.Entity>
{

    private com.codepath.apps.restclienttemplate.models.Entity entity$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<Entity$$Parcelable>CREATOR = new Creator<Entity$$Parcelable>() {


        @Override
        public Entity$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new Entity$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public Entity$$Parcelable[] newArray(int size) {
            return new Entity$$Parcelable[size] ;
        }

    }
    ;

    public Entity$$Parcelable(com.codepath.apps.restclienttemplate.models.Entity entity$$2) {
        entity$$0 = entity$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(entity$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.codepath.apps.restclienttemplate.models.Entity entity$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(entity$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(entity$$1));
            parcel$$1 .writeString(entity$$1 .loadUrl);
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.codepath.apps.restclienttemplate.models.Entity getParcel() {
        return entity$$0;
    }

    public static com.codepath.apps.restclienttemplate.models.Entity read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.codepath.apps.restclienttemplate.models.Entity entity$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            entity$$4 = new com.codepath.apps.restclienttemplate.models.Entity();
            identityMap$$1 .put(reservation$$0, entity$$4);
            entity$$4 .loadUrl = parcel$$3 .readString();
            com.codepath.apps.restclienttemplate.models.Entity entity$$3 = entity$$4;
            identityMap$$1 .put(identity$$1, entity$$3);
            return entity$$3;
        }
    }

}
