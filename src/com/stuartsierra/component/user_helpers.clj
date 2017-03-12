(ns com.stuartsierra.component.user-helpers
  "Helper functions to be referred into the 'user' namespace. These
  functions lazily load the tools.namespace and component.repl
  namespaces, to avoid delaying the start of a REPL.")

(defn- switch-to-dev []
  (in-ns 'dev)
  :ok)

(defn- invoke
  "Loads the namespace of a fully-qualified symbol, resolves the
  symbol, and invokes it with args. Used to lazily load library
  functions when they are called."
  [sym & args]
  (require (symbol (namespace sym)))
  (apply (resolve sym) args))

(defn dev
  "Loads all source files and switches to the 'dev' namespace."
  []
  (let [ret (invoke 'clojure.tools.namespace.repl/refresh :after `switch-to-dev)]
    (if (instance? Throwable ret)
      (throw ret)  ; let the REPL's exception handling take over
      ret)))

(defn go
  "Loads all source files, starts the application running in
  development mode, and switches to the 'dev' namespace."
  []
  (invoke 'com.stuartsierra.component.repl/reset)
  (switch-to-dev))

(defn reset
  "Alias for `go` to match the behavior of `reset` in the `dev`
  namespace."
  []
  (go))
