{
	inputs = {
		nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
	};

	outputs = { self, nixpkgs }:
		let
			system = "x86_64-linux";
			pkgs = import nixpkgs {
				inherit system;
			};

		in {
			devShells.${system}.default = pkgs.mkShell {
				packages = [
					pkgs.bashInteractive
					pkgs.jdk25
					pkgs.gradle
				];

				shellHook = ''
					export SHELL="${pkgs.bashInteractive}/bin/bash"
				'';
			};
		};
}

