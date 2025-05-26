Overview:
This system helps manage items by adding, deleting, searching, and undoing deletions. Each item has an ID, name, description, category, and price.

Data Structures Used:
Linked List:
The linked list stores all items. It makes adding and deleting items easy and fast. This list is the main place where items are kept.
Binary Search Trees (BST):
Two BSTs are used—one to search items by their ID and another to search by name. BSTs help find items quickly without searching through the whole list.
Queue:
A queue manages urgent items. Items marked as "urgent" are put into this queue and processed in order.
Stack:
A stack saves deleted items to allow undoing deletions. The most recently deleted item can be restored easily.
