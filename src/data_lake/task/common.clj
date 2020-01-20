(ns data-lake.task.common
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.shell :refer [sh]]
            [java-time :refer [offset-date-time]]
            [data-lake.consts :as c]
            [data-lake.core.sqlite :as sqlite]
            ))

(defn initiated? [] 
  (.exists (io/as-file c/history-db-path)))

(defn git-hash
  "Get current git-hash. If hash can't be retrieved,
  It return nil."
  []
  (try 
    (-> (sh "git" "rev-parse" "HEAD")
        :out (clojure.string/trim))
    (catch java.io.IOException ex 
      (println ex) 
      (println "Maybe git has not be installed"))))

(defn log [args]
  (if (.exists (io/as-file c/history-db-path))
      (jdbc/insert! 
        (sqlite/db-spec c/history-db-path)
        :history 
        {:cmd  (clojure.string/join " " args)
         :time (offset-date-time)
         :git_hash (git-hash)})))


(defmulti run-task (fn [& args] (when args (first args))))
