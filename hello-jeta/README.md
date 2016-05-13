How to create custom processors, step-by-step tutorial
------

### Step 1: `Hello, World` project

Let's create a simple `gradle` project with one module `app` and a single class `SayHelloApp`. This class writes `Hello, World!` to standard output.

![SayHelloApp](http://i.imgur.com/abvtUtm.png?1)

In this tutorial we'll create `Hello` annotation and will be providing `Hello, Jeta!` string to fields with this annotation.

### Step 2: `common` module

Now we need a module which will be accessible in `app` module and in `apt` module which we'll create shortly. In `common` module we'll create two classes - `Hello` annotation and `HelloMetacode` interface:

![common module](http://i.imgur.com/K2aQsgg.png)

### Step 3: `apt` module

`apt` - is a module in which we create all the required for code generation classes. For this tutorial we only need to create a processor that handles our `Hello` annotation.
