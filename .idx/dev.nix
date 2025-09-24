{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.maven
    pkgs.coreutils
  ];

  # Kept simple; applies to shells AND previews per the schema
  env = {
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    SPRING_PROFILES_ACTIVE = "dev";
    # Keep Gradle state local to the repo (helps avoid daemon/protocol clashes)
    GRADLE_USER_HOME = ".gradle-user";
  };

  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "vmware.vscode-boot-dev-pack"
      "google.gemini-cli-vscode-ide-companion"
    ];

    workspace = {
      onCreate = {
        # Fast + safe init; don't run a full build here
        prepare = ''
          set -e
          chmod +x ./gradlew 2>/dev/null || true
          chmod +x ./mvnw  2>/dev/null || true
          mkdir -p .gradle-user .gradle-tmp
          (GRADLE_USER_HOME="$PWD/.gradle-user" ./gradlew --version || true)
        '';
      };
      onStart = {
        verify-java = "java -version";
        default.openFiles = [ "src/main/AiChatServiceApplication.java" ];
      };
    };

    previews = {
      enable = true;

      previews = {
        # Gradle Spring Boot
        spring-gradle = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              mkdir -p .gradle-user .gradle-tmp
              chmod +x ./gradlew 2>/dev/null || true
              # Reassert in case preview runs without inherited env
              export GRADLE_USER_HOME="${GRADLE_USER_HOME:-$PWD/.gradle-user}"
              export GRADLE_OPTS="" MAVEN_OPTS="" _JAVA_OPTIONS="" JAVA_TOOL_OPTIONS=""
              exec ./gradlew \
                --no-daemon \
                --no-configuration-cache \
                -Djava.io.tmpdir="$PWD/.gradle-tmp" \
                -Dorg.gradle.java.installations.paths="$JAVA_HOME" \
                bootRun
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };

        # Optional Maven fallback
        spring-maven = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              chmod +x ./mvnw 2>/dev/null || true
              export MAVEN_OPTS="" _JAVA_OPTIONS="" JAVA_TOOL_OPTIONS=""
              exec ./mvnw -DskipTests spring-boot:run
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };
      };
    };
  };
}
