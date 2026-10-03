def call(Map config = [:]) {
    def target   = config.get('target', 'package.json')
    def toolName = config.get('toolName', 'DP-Check')

    stage('OWASP Dependency-Check') {
        script {
            try {
                dependencyCheck odcInstallation: toolName, 
                                additionalArguments: "--scan ${target} --format HTML --format XML --disableNodeAudit false --enableExperimental"
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml',
                                         stopBuild: false
            } catch (Exception e) {
                echo "OWASP Dependency-Check encountered an issue: ${e.message}"
            }
        }
    }
}
