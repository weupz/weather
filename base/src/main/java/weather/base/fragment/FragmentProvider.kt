package weather.base.fragment

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment

abstract class FragmentProvider : Parcelable {

    abstract fun providesFragment(): Fragment

    override fun writeToParcel(dest: Parcel?, flags: Int) {
    }

    override fun describeContents(): Int = 0

    companion object {
        inline fun <reified T : FragmentProvider> creator(
            crossinline create: () -> T
        ): Parcelable.ClassLoaderCreator<T> {
            return object : Parcelable.ClassLoaderCreator<T> {
                override fun createFromParcel(source: Parcel?, loader: ClassLoader?): T = create()

                override fun createFromParcel(source: Parcel?): T = create()

                override fun newArray(size: Int): Array<T?> = arrayOfNulls(size)
            }
        }
    }
}
