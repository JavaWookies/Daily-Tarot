# App Bar Fragment Code

## This goes into each activity layout file

```xml
<fragment
    android:id="@+id/app_bar_fragment" 
    android:name="your.package.name.AppBarFragment"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="top"
    app:layout_constraintTop_toTopOf="parent" />
```
