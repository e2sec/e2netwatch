package de.e2security.netflow_flowaggregation.model.protocols;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.e2security.netflow_flowaggregation.App;
import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;

public class NetflowEvent implements Serializable {
	private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private ZonedDateTime receivedTimeStamp; // logstash-generated
    private String host;
    private String ipv4_src_addr;
    private String ipv4_dst_addr;
    private Integer l4_src_port;
    private Integer l4_dst_port;
    private Integer tcp_flags;
    private Integer protocol; //38 :IP protocol type (TCP = 6; UDP = 17)
    private Integer version;  // logstash-generated
    private Integer flow_seq_num;
    private Integer flow_records;
    private Integer in_bytes;
    private Integer in_pkts;
    private ZonedDateTime first_switched;
    private ZonedDateTime last_switched;
    
	//factory method to create ordered instances from NetflowEvent (e.g. when the messages saved in ordered sequence)
	public NetflowEventOrdered convertToOrderedType() throws NetflowEventException {
		return new NetflowEventOrdered(
				this.receivedTimeStamp,
				this.host, 
				this.ipv4_src_addr, 
				this.ipv4_dst_addr, 
				this.l4_src_port, 
				this.l4_dst_port, 
				this.tcp_flags, 
				this.protocol, 
				this.version, 
				this.flow_seq_num,
				this.flow_records,
				this.in_bytes, 
				this.in_pkts, 
				this.first_switched, 
				this.last_switched);
	}

	public NetflowEvent(String jsonString) throws NetflowEventException {
        JSONObject jsonObject;
        JSONObject jsonNetflow;

		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			LOG.error(e.getMessage());
			LOG.error(String.format("String cannot be parsed as JSON: '%s'", jsonString));
            throw new NetflowEventException(e.getMessage());
		}

        try {
            this.receivedTimeStamp = ZonedDateTime.parse((String) jsonObject.get("@timestamp"));
        } catch (ClassCastException e) {
            LOG.error("'@timestamp' isn't a string.");
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

        try {
            this.host = (String) jsonObject.get("host");
        } catch (ClassCastException e) {
            LOG.error("'host' isn't a string.");
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

        try {
            jsonNetflow = (JSONObject) jsonObject.get("netflow");
        } catch (ClassCastException e) {
            LOG.error("'netflow' isn't a JSON sub-object.");
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

        try {
            this.ipv4_src_addr = (String) jsonNetflow.get("ipv4_src_addr");
            this.ipv4_dst_addr = (String) jsonNetflow.get("ipv4_dst_addr");
            this.l4_src_port = Integer.valueOf(jsonNetflow.getInt("l4_src_port"));
            this.l4_dst_port = Integer.valueOf(jsonNetflow.getInt("l4_dst_port"));
            this.tcp_flags = Integer.valueOf(jsonNetflow.getInt("tcp_flags"));
            this.protocol = Integer.valueOf(jsonNetflow.getInt("protocol"));
            this.version = Integer.valueOf(jsonNetflow.getInt("version"));
            this.flow_seq_num = Integer.valueOf(jsonNetflow.getInt("flow_seq_num"));
            this.flow_records = Integer.valueOf(jsonNetflow.getInt("flow_records"));
            this.in_bytes = Integer.valueOf(jsonNetflow.getInt("in_bytes"));
            this.in_pkts = Integer.valueOf(jsonNetflow.getInt("in_pkts"));
        } catch (ClassCastException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

        try {
        	this.first_switched = ZonedDateTime.parse((String) jsonNetflow.get("first_switched"));
        } catch (ClassCastException e) {
            LOG.error("'first_switched' isn't a string.");
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

        try {
        	this.last_switched = ZonedDateTime.parse((String) jsonNetflow.get("last_switched"));
        } catch (ClassCastException e) {
            LOG.error("'last_switched' isn't a string.");
            throw new NetflowEventException(e.getMessage());
        } catch (JSONException e) {
            LOG.error(e.getMessage());
            throw new NetflowEventException(e.getMessage());
        }

	}
	
    public ZonedDateTime getReceivedTimeStamp() {
        return receivedTimeStamp;
    }
    
    public String getHost() {
        return host;
    }
    
    public String getIpv4_src_addr() {
        return ipv4_src_addr;
    }
    
    public String getIpv4_dst_addr() {
        return ipv4_dst_addr;
    }
    
    public Integer getL4_src_port() {
        return l4_src_port;
    }
    
    public Integer getL4_dst_port() {
        return l4_dst_port;
    }
    
    public Integer getTcp_flags() {
        return tcp_flags;
    }
    
    public Integer getProtocol() {
        return protocol;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public Integer getFlow_seq_num() {
        return flow_seq_num;
    }
    
    public Integer getFlow_records() {
        return flow_records;
    }
    
    public Integer getIn_bytes() {
        return in_bytes;
    }
    
    public Integer getIn_pkts() {
        return in_pkts;
    }
    
    public ZonedDateTime getFirst_switched() {
        return first_switched;
    }
    
    public ZonedDateTime getLast_switched() {
        return last_switched;
    }
}
