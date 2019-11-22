(ns data-lake.task.sqlite
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [clojure.java.io :as io] ;TODO: remove
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
  (let [db-spec {:classname   "org.sqlite.JDBC"
                 :subprotocol "sqlite"
                 :subname     path}]
    (when (db-do-noexcept db-spec ddl) db-spec)))


(comment
(def db (create! "DB/tmp.db"
                 (jdbc/create-table-ddl
                   :annotation
                   [[:input  :BLOB]
                    [:output :BLOB]])))
(def inp (java.util.UUID/randomUUID))
(def out (java.util.UUID/randomUUID))
(print db)

(jdbc/insert! db :annotation {:input inp :output out})
(jdbc/insert-multi! db
                    :annotation
                    [{:input inp :output out}
                     {:input inp :output out}
                     {:input inp :output out}])
(jdbc/query db (-> (h/select :%count.*) 
                   (h/from :annotation)
                   sql/format))
;(edn/read-string (slurp

(jdbc/create-table-ddl
  :file
  [[:guid      :BLOB    :NOT :NULL :PRIMARY :KEY]
   [:path      :TEXT    :NOT :NULL]
   [:extension :TEXT    :NOT :NULL]
   [:size      :INTEGER :NOT :NULL]
   ["FOREIGN KEY(guid)" :REFERENCES "data(id)"]])
)

;(.delete (io/as-file (:subname db)))
