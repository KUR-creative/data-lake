(ns data-lake.task.sqlite
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            )
  (:gen-class))

(defn db-do-noexcept
  "Do cmd on db"
  [db cmd]
  (try (jdbc/db-do-commands db cmd) 
       (catch Exception e
              (println (.getMessage e)))));TODO: logging

(defn create!
  "Create db. If specified db is already exists, 
   it never create."
  [path ddl]
  (db-do-noexcept {:classname   "org.sqlite.JDBC"
                   :subprotocol "sqlite"
                   :subname     path}
                  ddl))
