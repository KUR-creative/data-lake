(ns data-lake.task.sqlite
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [clojure.tools.trace :as dbg]
            [orchestra.spec.test :as st]
            ;[clojure.tools.trace :as dbg]
            )
  (:gen-class))

(s/def ::table-name (s/or :kw keyword? :s string?))
(s/def ::table-spec (s/* vector?))
(s/def ::table-schema (s/tuple ::table-name 
                               ::table-spec))
(s/fdef create!
  :args (s/cat :path string? 
               :schema (s/coll-of ::table-schema)))

(defn create!
  "Create sqlite db in `path` using schema 
  described in `schema`"
  [path schema]
  (let [db {:classname   "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname     path}]
    (jdbc/db-do-commands
      db (mapv #(apply jdbc/create-table-ddl %) schema))
    db))

(defn run-cmd 
  "Run command"
  [& args]
  (vec args)
  (cond 
    (= (first args) "new") 
    (let [[_ db-path edn-path] args
          schema (-> edn-path
                     slurp edn/read-string :schema)]
      ;(create! db-path schema))
      (create! db-path (dbg/trace schema)))

    :else
    (println "wtf" args (first args))
    ))
