(ns data-lake.task.add-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [data-lake.cli :as cli]
            [data-lake.task.common :as tc]
            [data-lake.core.sqlite :as cs]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            ;[data-lake.core.sqlite :as sqlite]
            ))

(deftest task-add-test
  (testing "number of args"
    (is (thrown? IllegalArgumentException
                 (cli/run "add"))))
  (testing "<source> are an image"
    (let [source  "./test/fixture/wr70x85.png"
          target  "./test/temp/test.db" 
          db-file (io/as-file target)
          edn     "./DB/sqlite/szmc_schema_0.1.0.edn"
          db      (cs/create! target (cs/schema-map edn))
          query   (-> (h/select :path)
                      (h/from :file)
                      sql/format)]
      (tc/run-task "add" target source :no-log)
      (is (= source (-> (jdbc/query db query)
                        first :path)))
      (.delete db-file))))
