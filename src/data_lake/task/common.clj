(ns data-lake.task.common
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [java-time :refer [offset-date-time]]
            [data-lake.consts :as c]
            [data-lake.core.sqlite :as sqlite]
            ))

(defn initiated? [] 
  (.exists (io/as-file c/history-db-path)))

(defn log [args]
  (if (.exists (io/as-file c/history-db-path))
      (jdbc/insert! (sqlite/db-spec c/history-db-path)
                    :history 
                    {:cmd  (clojure.string/join " " args)
                     :time (offset-date-time)})))

(defmulti run-task (fn [& args] (when args (first args))))
