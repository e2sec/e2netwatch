<?php

/*
    Copyright (C) 2017 e-ito Technology Services GmbH
    e-mail: info@e-ito.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

$json = '{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "@timestamp": {
               "gte": "' . $startdate . '",
               "lte": "' . $enddate . '"
            }
          }
        },
        {
          "wildcard": {
            "TCPFlags": {
              "value": "....S."
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "ipaddress": {
      "terms": {
        "field": "SourceAddress",
        "size": 10,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}';


$params = array(
    'index' => 'e2nw-netflow-*',
     'body' => $json
);

$response = $elastic->search($params);
$file = fopen("csv/410_Sources_Sending_SYN_Only_a.csv", "w");
// Check if Elasticsearch was able to provide any result
if (isset($response['aggregations']['ipaddress']['buckets'][0]['key'])) {
    $txt = 'Source IP Address,Count' . PHP_EOL;
    foreach ($response['aggregations']['ipaddress']['buckets'] as $data) {
         $txt .= $data['key'] . ',' . number_format($data['doc_count'],0,'','.') . PHP_EOL;
    }
} else {
    $txt = 'Source IP Address,Count' . PHP_EOL;
    $txt .= "n/a,n/a" . PHP_EOL;
}


fwrite($file, $txt);
fclose($file);



?>
