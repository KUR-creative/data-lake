(ns util.db-util
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            ))

(s/def ::order list?)
(s/def ::schema map?)
(s/def ::schema-map (s/keys :req-un [::order ::schema]))
(s/explain ::schema-map {:order 1 :schema 2})

(s/def ::a #(= % 1))
(s/def ::b #{2})
(s/explain ::a 1)
(s/explain ::b 2)

(s/def ::dat (s/keys :req-un [::a ::b]))
(s/explain ::dat {:a 1 :b 2})
(s/explain ::dat {:a 1})

(defn ddl-seq
  "Read jdbc/create-table-ddl style schema from edn file.
   Return sequence of create-table-ddl"
  [edn])

(def m (edn/read-string (slurp "./DB/sqlite/szmc_schema_0.1.0.edn")))
(s/valid? ::schema-map m)
(s/explain ::schema-map m)
