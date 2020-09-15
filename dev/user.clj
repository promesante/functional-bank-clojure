(ns user
  (:require [clojure.tools.namespace.repl :as tn]
            [mount.core :as mount]
            [bank.effects]
   ))

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start #'bank.effects/registry))

(defn stop
  "Stops application."
  []
  (mount/stop))

(defn go
  "starts all states defined by defstate"
  []
  (start)
  :ready)

(defn reset
  "Restarts application."
  []
  (stop)
  (tn/refresh :after 'user/start)
  ; (tn/refresh :after 'user/go)
  )
