# paging-listview
The listview contains feed items which consists of an image and headline. The data is fetched in a URL containing a JSON array. The code is fetching those arrays using rxAndroid framework. 

The image is provided as a URL in the JSON array therefore, before displaying into the listview, it must be downloaded and coverted into bitmap first. An open-source library called Glide is used for managing the images and to efficiently display it into the listview.

The list starts from 10 items and when scrolled down to the last item, it fetches the next 10 batch and add it to the list.
