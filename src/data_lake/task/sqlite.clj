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
(s/def ::schema-map (s/coll-of ::table-schema))
(s/fdef create!
  :args (s/cat :path string? :schema ::schema-map))

(defn db-spec 
  "Sqlite DB Spec map"
  [path]
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     path})

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

(s/fdef schema-map 
  :args (s/cat :edn-path string?)
  :ret ::schema-map)
(defn schema-map [edn-path]
  "schema edn path -> schema map"
  (-> edn-path slurp edn/read-string :schema))

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

    :else false)) ;; Failure case
