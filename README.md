# File zipper

Implements file compression in a way that abstracts the hierarchical storage. The actual implementation is based on a
filesystem but can be easily adapted to other storage systems that are not filesystems. It uses generics and Java 8 
lambda expressions / method references to describe the storage in a generic way.
 