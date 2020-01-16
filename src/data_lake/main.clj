(ns data-lake.main
  (:require [util.db-util :as dbu]
            [clojure.tools.cli :as cli])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option (:default is applied first)
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :update-fn inc] ; Prior to 0.4.1, you would have to use:
                   ;; :assoc-fn (fn [m k _] (update-in m [k] inc))
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println args "Hello, World!")
  (clojure.pprint/pprint (cli/parse-opts args cli-options)))
