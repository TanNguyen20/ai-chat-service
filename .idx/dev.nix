{ pkgs, ... }: {
  channel = "stable-24.05";

  packages = [
    pkgs.jdk17
    pkgs.gradle
    pkgs.maven
    pkgs.coreutils
  ];

  # keep globals minimal – no JAVA_TOOL_OPTIONS here
  env = {
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
    SPRING_PROFILES_ACTIVE = "dev";
  };

  idx = {
    extensions = [
      "vscjava.vscode-java-pack"
      "vmware.vscode-boot-dev-pack"
      "google.gemini-cli-vscode-ide-companion"
    ];

    workspace = {
      # Make onCreate *fast* and failure-proof
      onCreate = {
        prepare = ''
          set -e
          chmod +x ./gradlew 2>/dev/null || true
          chmod +x ./mvnw  2>/dev/null || true
          mkdir -p .gradle-user .gradle-tmp
          # Do NOT run a build here; just verify the wrapper prints a version.
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
        spring-gradle = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              mkdir -p .gradle-user .gradle-tmp
              chmod +x ./gradlew 2>/dev/null || true
              export GRADLE_USER_HOME="$PWD/.gradle-user"
              export GRADLE_OPTS=""
              export MAVEN_OPTS=""
              export _JAVA_OPTIONS=""
              export JAVA_TOOL_OPTIONS=""
              # harden the run (no reused state / wire-protocol clashes)
              exec ./gradlew \
                --no-daemon \
                --no-configuration-cache \
                -Djava.io.tmpdir="$PWD/.gradle-tmp" \
                bootRun
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };

        # optional Maven fallback
        spring-maven = {
          manager = "gradle";
          cwd = "";
          command = [
            "bash" "-lc"
            ''
              set -euo pipefail
              chmod +x ./mvnw 2>/dev/null || true
              export MAVEN_OPTS=""
              export _JAVA_OPTIONS=""
              export JAVA_TOOL_OPTIONS=""
              exec ./mvnw -DskipTests spring-boot:run
            ''
          ];
          env = { SPRING_PROFILES_ACTIVE = "dev"; };
        };
      };
    };
  };
}
