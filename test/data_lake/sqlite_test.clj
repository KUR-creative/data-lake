(ns data-lake.sqlite-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [data-lake.task.sqlite :refer :all]))

(deftest create!-test
  (testing "create sqlite db file"
    (let [path "./test/fixture/test.db" 
          file (io/as-file path)
          _    (create! path
                        (jdbc/create-table-ddl
                          :file
                          [[:test :INTEGER]]))]
      (do (is (.exists file))
          (.delete file)
          )))
  (testing "if successfully creation then return db-spec"
    (let [path "./test/fixture/test2.db"]
      (is (= {:classname   "org.sqlite.JDBC"
              :subprotocol "sqlite"
              :subname     path}
             (create! path
                      (jdbc/create-table-ddl
                        :file
                        [[:test :INTEGER]]))))
      (.delete (io/as-file path))))
  (testing "if can't create db then return nil"
    (is (nil? (create! nil nil)))))

(deftest szmc-sqlite-v0.1.0-schema-test
  (testing "create szmc v0.1.0 sqlite db from edn"
    (let [path "./test/fixture/test3.db" 
          file (io/as-file path)
    )
  )
