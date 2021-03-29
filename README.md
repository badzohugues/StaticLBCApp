# StaticLBCApp

StaticLBC is an Android application which fetching data from web service and store it offline. 

In this project I used a modern stack with features like :

* Google Architecture Components(MVVM, Navigation components, View Binding Databinding)
* Coroutines for asynchronous tasks
* Room for storing
* Dagger Hilt for Dependencies Injection

## Application's behaviour

The synchronization works like this: 
* if there's a network connection the app will always fetching data from web service and store in database.
* if no connection the app displays offline datas.

The first page shows a list of albums, and the second page displays items of the album

## Source
[Coroutines](https://developer.android.com/codelabs/kotlin-coroutines#9)

[Room database](https://developer.android.com/codelabs/android-room-with-a-view-kotlin#5)

[Unit test](https://www.youtube.com/watch?v=EkfVL5vCDmo&list=PLQkwcJG4YTCSYJ13G4kVIJ10X5zisB2Lq)
