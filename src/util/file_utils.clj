(ns util.file-utils
  (:require [me.raynes.fs :as fs]))

(defn ext-without-dot [path]
  (-> path fs/extension rest clojure.string/join))
