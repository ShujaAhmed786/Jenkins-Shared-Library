def call(Map config = [:]) {
    def imageName   = config.imageName
    def clusterName = config.get('clusterName', 'kind')
    def deployment  = config.get('deployment', 'ratestack-deployment')
    def namespace   = config.get('namespace', 'default')

    stage('Deploy to Kind') {
        sh "kind load docker-image ${imageName}:latest --name ${clusterName}"
        sh "kubectl rollout restart deployment/${deployment} -n ${namespace}"
    }
}
