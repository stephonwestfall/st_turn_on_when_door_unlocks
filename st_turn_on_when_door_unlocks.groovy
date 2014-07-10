/**
 *  Turn On When Door Unlocks
 *
 *  Copyright 2014 skp19
 *
 */
definition(
    name: "Turn On When Door Unlocks",
    namespace: "skp19",
    author: "skp19",
    description: "Turns on a device when the door is unlocked",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Which door locks?") {
		input "lock1", "capability.lock", multiple: true
	}
    section("Which switches?") {
    	input "switches", "capability.switch", multiple: true
    }
    section("Turn on or off?") {
    	input "turnon", "bool", title: "Turn on when door unlocks?"
        input "turnoff", "bool", title: "Turn off when door locks?"
    }
    section("Delay turning off") {
    	input "turnoffdelay", "number", title: "Minutes", required: false
    }
}

def installed() {
    subscribe(lock1, "lock", turniton)
}

def updated() {
	unsubscribe()
    subscribe(lock1, "lock", turniton)
}

def turniton(evt) {
    //log.debug "$evt.value: $evt, $settings"
    if(turnon) {
    	if(evt.value == "unlocked") {
			log.trace "Turning on switches: $switches"
        	switches.on()
		}
    }
    if(turnoff) {
    	if(evt.value == "locked") {
            if(turnoffdelay) {
            	log.trace "Waiting to turn off switches for $turnoffdelay minutes"
	            def turnOffDelaySeconds = 60 * turnoffdelay
				runIn(turnOffDelaySeconds, turnitoff)
            }
        	else {
        		turnitoff()
            }
        }
    }
}

def turnitoff() {
	log.trace "Turning off switches: $switches"
	switches.off()
}
