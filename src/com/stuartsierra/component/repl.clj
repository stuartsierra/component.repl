(ns com.stuartsierra.component.repl
  "REPL utilities for the Component framework. Provides the global
  `system` under development and the REPL lifecycle functions `reset`,
  `start`, and `stop`. You must provide a system constructor: see
  `set-init`."
  (:require
   [com.stuartsierra.component :as component]
   [clojure.tools.namespace.repl :refer [refresh]]))

(def system
  "The system under development"
  nil)

(def initializer
  "No-argument function to return the initial (unstarted) system map
  for use during interactive development. Use `set-init` in your
  development namespace to provide an initializer function."
  (fn [system]
    (throw (ex-info "initializer not set, did you call `set-init`?"
                    {}))))

(defn set-init
  "Specifies the user-defined initializer function to use for
  constructing the system. Call `set-init` at the top-level in your
  development namespace. init-fn is a function of one argument, the
  previous (stopped) system map, which will be nil the first time it
  is called. init-fn should return an (unstarted) component system
  map. Most init-fn implementations will ignore the argument and
  simply construct a new system map, but you may optionally copy state
  from the old system."
  [init-fn]
  (alter-var-root #'initializer (constantly init-fn)))

(defn start
  "Initializes and starts a new system running, updates #'system"
  []
  (alter-var-root #'system initializer)
  (alter-var-root #'system component/start)
  :ok)

(defn stop
  "Stops the system if it is currently running, updates #'system. Any
  exception thrown during `component/stop` will be printed but
  otherwise ignored."
  []
  (alter-var-root #'system
                  (fn [sys]
                    (when sys
                      (try (component/stop sys)
                           (catch Throwable t
                             (prn t)
                             sys)))))
  :ok)

(defn reset
  "Stops the system, reloads modified source files, and restarts the
  system."
  []
  (stop)
  (let [ret (refresh :after `start)]
    (if (instance? Throwable ret)
      (throw ret)  ; let the REPL's exception handling take over
      ret)))
