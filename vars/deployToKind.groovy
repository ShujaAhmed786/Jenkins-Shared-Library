def call(Map config = [:]) {
    def clusterName    = config.get('clusterName', 'kind')
    def deploymentName = config.get('deploymentName', 'ratestack-deployment')
    def namespace      = config.get('namespace', 'default')
    def imageName      = config.get('imageName', "shujaahmed198/ratestack-app:${env.BUILD_NUMBER}")

    stage('Deploy to Kind') {
        // If loading locally into Kind:
        sh "kind load docker-image ${imageName} --name ${clusterName}"

        // Update the Kubernetes deployment
        sh """
            kubectl set image deployment/${deploymentName} \
              ${deploymentName}=${imageName} \
              -n ${namespace} --record || kubectl rollout restart deployment/${deploymentName} -n ${namespace}
        """
    }
}
