package no.fint.relations.config

import spock.lang.Specification

class FintRelationsPropsSpec extends Specification {

    def "Get local server port"() {
        when:
        def props = new FintRelationsProps(relationBase: 'http://localhost')

        then:
        props.relationBase == 'http://localhost'
    }

    def "Get local server address"() {
        when:
        def props = new FintRelationsProps(relationBase: 'http://localhost:8080')

        then:
        props.relationBase == 'http://localhost:8080'
    }

    def "Use local server port when configured"() {
        when:
        def props = new FintRelationsProps(localServerPort: 1234)
        props.init()

        then:
        props.getPort() == 1234
    }

    def "Use server port when configured"() {
        when:
        def props = new FintRelationsProps(serverPort: 2345)
        props.init()

        then:
        props.getPort() == 2345
    }
}
