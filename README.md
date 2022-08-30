# AppImageLoader
Android Library, load image from resource/assets/uri/http/url

### examples

```kotlin
imageView.loadRes(R.mipmap.cat1) {}
imageView.loadAsset("imgs/cat1.jpg") {}
imageView.loadURL("http://app800.cn/cat1.jpg") {}
imageView.loadURL("http://app800.cn/cat1.jpg") {
    maxEdge = 500
    lowQuility()
}
imageView.loadUri(uri){}
```