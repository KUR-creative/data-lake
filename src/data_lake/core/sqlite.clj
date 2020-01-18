(ns data-lake.core.sqlite
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [clojure.java.jdbc :as jdbc]))
            

(defn db-spec 
  "Sqlite DB Spec map"
  [path]
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     path})

(s/def ::table-name (s/or :kw keyword? :s string?))
(s/def ::table-spec (s/* vector?))
(s/def ::table-schema (s/tuple ::table-name 
                               ::table-spec))
(s/def ::schema-map (s/coll-of ::table-schema))
(s/fdef create!
  :args (s/cat :path string? :schema ::schema-map))

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
