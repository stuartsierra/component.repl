# component.repl

Utilities for interactive development using the [Component] framework
and [tools.namespace].

This is an extension of the development workflow first described in
[Workflow Reloaded] and codified in the [reloaded] template.

[Component]: https://github.com/stuartsierra/component
[tools.namespace]: https://github.com/clojure/tools.namespace
[Workflow Reloaded]: http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded
[reloaded]: https://github.com/stuartsierra/reloaded



## Releases and Dependency Information

* Releases are published to [Clojars](https://clojars.org/com.stuartsierra/component.repl)

* Latest stable release is [0.2.0](https://clojars.org/com.stuartsierra/component.repl/versions/0.2.0)

* [All released versions](https://clojars.org/com.stuartsierra/component.repl/versions)

[Leiningen] dependency information:

    [com.stuartsierra/component.repl "0.2.0"]

[Maven] dependency information:

    <dependency>
      <groupId>com.stuartsierra</groupId>
      <artifactId>component.repl</artifactId>
      <version>0.2.0</version>
    </dependency>

[Leiningen]: http://leiningen.org/
[Maven]: http://maven.apache.org/



## Usage

Require `com.stuartsierra.component.repl` in your development
namespace.

```clojure
(ns dev
  (:require
   [com.stuartsierra.component :as component]
   [com.stuartsierra.component.repl
     :refer [reset set-init start stop system])))
```

Provide an initializer function to
`com.stuartsierra.component.repl/set-init` to construct a Component
system map. The initializer function takes one argument, the old
system, usually ignored.

```clojure
(defn new-system [_]
  (component/system-map
    :server (web-server)
    :database (database)
    :cache (cache)))

(set-init new-system)
```

At the REPL, use the `com.stuartsierra.component.repl/reset` function
to reload modified source files and restart your system.

    example=> (reset)
    :reloading (example)
    :ok

The current system under development is available as the Var
`com.stuartsierra.component.repl/system`.

    example=> (keys system)
    (:server :database :cache)



## Bootstrapping the `user` namespace

Clojure will automatically load a file named named `user.clj` on REPL
start-up. Usually it is not a good idea to do to much in `user.clj`,
because any exception thrown while loading `user.clj` will prevent the
REPL from starting.

The namespace `com.stuartsierra.component.user-helpers` provides a few
convenience functions that you can `:refer` into the `user` namespace
to quickly "boot" the component.repl system.

The `user-helpers` namespace has no load-time dependencies: It will
lazily load other namespaces as needed, so it does not interfere with
fast startup of the REPL itself.

To use these helpers, place a `user.clj` file on the classpath (in a
directory that will **not** be packaged with your application or
library) containing the following:

```clojure
(ns user
  (:require
   [com.stuartsierra.component.user-helpers :refer [dev go reset]]))
```

Then, after starting a REPL, you can immediately run one of these
functions:

* `(go)` or `(reset)` to load all source code, start the component
  system running, and switch to the `dev` namespace.

* `(dev)` to just load code and switch to `dev` without starting the
  system.

If your develompent namespace is not named `dev`, you can call
`set-dev-ns` with the (symbol) name of your development namespace:

```clojure
(ns user
  (:require
   [com.stuartsierra.component.user-helpers 
    :refer [dev go reset set-dev-ns]]))

(set-dev-ns 'com.example.my-app-dev)
```



## Contributions and Forks

I created this library for my own personal use, and I am publishing it
in the hopes that others will find it useful. I encourage you to fork
and modify this library to suit your own needs and personal workflow,
but I am unlikely to merge any changes into this repository.



## Copyright and License

MIT License

Copyright (c) 2017 Stuart Sierra

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
