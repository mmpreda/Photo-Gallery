# Photo-Gallery

### App architecture

I chose the MVVM pattern. Here is a summary:
- "GalleryDatabase" - Room database
- "GalleryApiService" - implementation of the API endpoints
- "GalleryRepository" - to handle access to the database and to the API endpoints
- "AlbumsViewModel" - the app's view model for the albums screen, with an instance of the repository
- "PhotosViewModel" - the app's view model for the photos screen, with an instance of the repository
- view level : "MainActivity" hosting two fragments - "AlbumsFragment" and "PhotosFragment"


### Libraries used

- Jetpack: Navigation, Lifecycle, Room
Used for the essential aspects of navigation (with arguments), lifecycle management and database management.
- Retrofit
- Picasso
